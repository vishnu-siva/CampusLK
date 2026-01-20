import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:stomp_dart_client/stomp_dart_client.dart';

import '../../core/api/api_client.dart';
import '../../core/storage/token_storage.dart';
import '../../core/utils/api_response.dart';
import 'notification_badge_controller.dart';
import '../../models/notification_message.dart';
import '../../models/student.dart';

class NotificationsScreen extends StatefulWidget {
  const NotificationsScreen({super.key});

  @override
  State<NotificationsScreen> createState() => _NotificationsScreenState();
}

class _NotificationsScreenState extends State<NotificationsScreen> {
  final List<NotificationMessage> _messages = [];
  StompClient? _client;

  Student? _student;
  String _status = 'Loading profile...';
  bool _connected = false;
  bool _loadingList = true;

  @override
  void initState() {
    super.initState();
    _loadStudentAndConnect();
  }

  @override
  void dispose() {
    _disconnect();
    super.dispose();
  }

  Future<void> _loadStudentAndConnect() async {
    setState(() => _status = 'Loading profile...');

    try {
      final res = await ApiClient.get('/api/students/me');
      if (!mounted) return;

      if (res.statusCode != 200) {
        setState(() => _status = 'Profile load failed: ${res.statusCode}');
        return;
      }

      final json = jsonDecode(res.body);
      final api = ApiResponse.fromJson(json);
      final student = Student.fromJson(api.data);

      setState(() => _student = student);
      await _loadAllNotifications(student.id);
      await _refreshUnreadCount(student.id);
      _connect();
    } catch (e) {
      if (!mounted) return;
      setState(() => _status = 'Profile load error: $e');
    }
  }

  Future<void> _connect() async {
    if (_student == null) return;

    _disconnect();
    setState(() => _status = 'Connecting...');

    final token = await TokenStorage.getToken();
    final headers = token == null ? <String, String>{} : {'Authorization': 'Bearer $token'};

    final stompUrl = '${ApiClient.baseUrl}/ws';

    _client = StompClient(
      config: StompConfig.sockJS(
        url: stompUrl,
        stompConnectHeaders: headers,
        webSocketConnectHeaders: headers,
        onConnect: (StompFrame frame) {
          final studentId = _student?.id;
          if (studentId == null) return;

          final destination = '/topic/notifications/$studentId';
          _client?.subscribe(
            destination: destination,
            callback: (message) {
              if (message.body == null) return;
              final parsed = _parseMessage(message.body!, studentId);
              if (!mounted) return;
              setState(() => _messages.insert(0, parsed));
              NotificationBadgeController.increment();
            },
          );

          if (!mounted) return;
          setState(() {
            _connected = true;
            _status = 'Connected';
          });
        },
        onDisconnect: (_) {
          if (!mounted) return;
          setState(() {
            _connected = false;
            _status = 'Disconnected';
          });
        },
        onWebSocketError: (dynamic err) {
          if (!mounted) return;
          setState(() {
            _connected = false;
            _status = 'WebSocket error: $err';
          });
        },
        onStompError: (frame) {
          if (!mounted) return;
          setState(() {
            _connected = false;
            _status = 'STOMP error: ${frame.body}';
          });
        },
      ),
    );

    _client?.activate();
  }

  void _disconnect() {
    _client?.deactivate();
    _client = null;
    if (mounted) {
      setState(() {
        _connected = false;
        _status = 'Disconnected';
      });
    }
  }

  Future<void> _loadAllNotifications(String studentId) async {
    setState(() => _loadingList = true);
    try {
      final res = await ApiClient.get('/api/notifications/all/$studentId');
      if (!mounted) return;

      if (res.statusCode != 200) {
        setState(() => _loadingList = false);
        return;
      }

      final list = jsonDecode(res.body);
      if (list is List) {
        setState(() {
          _messages
            ..clear()
            ..addAll(
              list.map((item) {
                final map = item is Map<String, dynamic>
                    ? item
                    : (item as Map).cast<String, dynamic>();
                return NotificationMessage.fromJson(map);
              }),
            );
          _loadingList = false;
        });
      } else {
        setState(() => _loadingList = false);
      }
    } catch (_) {
      if (!mounted) return;
      setState(() => _loadingList = false);
    }
  }

  Future<void> _refreshUnreadCount(String studentId) async {
    try {
      final res = await ApiClient.get('/api/notifications/unread/$studentId');
      if (res.statusCode != 200) return;

      final list = jsonDecode(res.body);
      if (list is List) {
        NotificationBadgeController.unreadCount.value = list.length;
      }
    } catch (_) {}
  }

  Future<void> _markAsRead(NotificationMessage message) async {
    final id = message.id;
    if (id == null || id.isEmpty) return;

    try {
      await ApiClient.put('/api/notifications/$id/read', {});
      final studentId = _student?.id;
      if (studentId == null) return;
      await _refreshUnreadCount(studentId);
    } catch (_) {}
  }

  NotificationMessage _parseMessage(String body, String studentId) {
    try {
      final decoded = jsonDecode(body);
      final map = decoded is Map<String, dynamic>
          ? decoded
          : (decoded as Map).cast<String, dynamic>();
      return NotificationMessage.fromJson(map);
    } catch (_) {
      return NotificationMessage(
        title: 'Raw',
        body: body,
        category: 'raw',
        targetStudentId: studentId,
        createdAt: DateTime.now().toIso8601String(),
      );
    }
  }

  String _formatTimestamp(String? raw) {
    if (raw == null || raw.isEmpty) return '';
    final parsed = DateTime.tryParse(raw);
    if (parsed == null) return raw;
    final local = parsed.toLocal();
    final year = local.year.toString().padLeft(4, '0');
    final month = local.month.toString().padLeft(2, '0');
    final day = local.day.toString().padLeft(2, '0');
    final hour = local.hour.toString().padLeft(2, '0');
    final minute = local.minute.toString().padLeft(2, '0');
    return '$year-$month-$day $hour:$minute';
  }

  @override
  Widget build(BuildContext context) {
    final studentName = _student?.name ?? 'Student';

    return Scaffold(
      appBar: AppBar(
        title: const Text('Notifications'),
        actions: [
          IconButton(
            tooltip: _connected ? 'Disconnect' : 'Reconnect',
            icon: Icon(_connected ? Icons.link_off : Icons.link),
            onPressed: _connected ? _disconnect : _connect,
          ),
        ],
      ),
      body: Column(
        children: [
          Container(
            width: double.infinity,
            padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
            color: Theme.of(context).colorScheme.surfaceContainerHighest,
            child: Row(
              children: [
                Icon(
                  _connected ? Icons.check_circle : Icons.info,
                  color: _connected ? Colors.green : Colors.orange,
                ),
                const SizedBox(width: 10),
                Expanded(
                  child: Text(
                    _status,
                    style: const TextStyle(fontWeight: FontWeight.w600),
                  ),
                ),
                Text(
                  studentName,
                  style: const TextStyle(fontSize: 12, color: Colors.black54),
                ),
              ],
            ),
          ),
          Expanded(
            child: _loadingList
                ? const Center(child: CircularProgressIndicator())
                : _messages.isEmpty
                    ? const Center(child: Text('No notifications yet'))
                : ListView.separated(
                    padding: const EdgeInsets.symmetric(vertical: 8),
                    itemCount: _messages.length,
                    separatorBuilder: (_, __) => const Divider(height: 1),
                    itemBuilder: (context, index) {
                      final message = _messages[index];
                      final timestamp = _formatTimestamp(message.createdAt);
                      return ListTile(
                        leading: Icon(
                          message.read ? Icons.notifications_none : Icons.notifications,
                          color: message.read ? Colors.grey : Theme.of(context).primaryColor,
                        ),
                        title: Text(message.title ?? 'Notification'),
                        subtitle: Text(message.body ?? ''),
                        trailing: Text(
                          timestamp,
                          style: const TextStyle(fontSize: 12, color: Colors.black54),
                        ),
                        onTap: () => _markAsRead(message),
                      );
                    },
                  ),
          ),
        ],
      ),
    );
  }
}
