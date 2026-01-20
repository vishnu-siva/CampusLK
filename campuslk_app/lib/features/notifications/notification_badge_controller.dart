import 'dart:convert';

import '../../core/api/api_client.dart';
import '../../core/utils/api_response.dart';
import '../../models/student.dart';
import 'package:flutter/foundation.dart';

class NotificationBadgeController {
  static final ValueNotifier<int> unreadCount = ValueNotifier<int>(0);

  static Future<void> refreshUnreadCount() async {
    try {
      final res = await ApiClient.get('/api/students/me');
      if (res.statusCode != 200) return;

      final json = jsonDecode(res.body);
      final api = ApiResponse.fromJson(json);
      final student = Student.fromJson(api.data);

      final unreadRes = await ApiClient.get('/api/notifications/unread/${student.id}');
      if (unreadRes.statusCode != 200) return;

      final list = jsonDecode(unreadRes.body);
      if (list is List) {
        unreadCount.value = list.length;
      }
    } catch (_) {}
  }

  static void increment() {
    unreadCount.value = unreadCount.value + 1;
  }

  static void reset() {
    unreadCount.value = 0;
  }
}
