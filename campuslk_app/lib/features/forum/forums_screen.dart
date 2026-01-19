import 'dart:convert';
import 'package:flutter/material.dart';
import '../../core/api/api_client.dart';
import 'threads_screen.dart';

class ForumsScreen extends StatefulWidget {
  const ForumsScreen({super.key});

  @override
  State<ForumsScreen> createState() => _ForumsScreenState();
}

class _ForumsScreenState extends State<ForumsScreen> {
  bool loading = true;
  String? error;
  List forums = [];

  @override
  void initState() {
    super.initState();
    loadForums();
  }

  Future<void> loadForums() async {
    setState(() {
      loading = true;
      error = null;
    });

    try {
      final res = await ApiClient.get('/api/forums');
      if (!mounted) return;

      if (res.statusCode != 200) {
        String msg = "Failed (${res.statusCode})";
        try {
          msg = jsonDecode(res.body)['message'] ?? msg;
        } catch (_) {}
        setState(() {
          loading = false;
          error = msg;
          forums = [];
        });
        return;
      }

      final body = jsonDecode(res.body);
      setState(() {
        forums = body['data'] ?? [];
        loading = false;
      });
    } catch (e) {
      if (!mounted) return;
      setState(() {
        loading = false;
        error = "Network error: $e";
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    if (loading) {
      return const Scaffold(body: Center(child: CircularProgressIndicator()));
    }

    if (error != null) {
      return Scaffold(
        appBar: AppBar(title: const Text("Forums")),
        body: Center(
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              Text(error!, textAlign: TextAlign.center),
              const SizedBox(height: 12),
              ElevatedButton(onPressed: loadForums, child: const Text("Retry")),
            ],
          ),
        ),
      );
    }

    return Scaffold(
      appBar: AppBar(title: const Text("Forums")),
      body: forums.isEmpty
          ? const Center(child: Text("No forums yet"))
          : ListView.builder(
              itemCount: forums.length,
              itemBuilder: (context, i) {
                final f = forums[i];
                return ListTile(
                  title: Text(f['title'] ?? ''),
                  subtitle: Text(f['description'] ?? ''),
                  trailing: const Icon(Icons.chevron_right),
                  onTap: () async {
                    await Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (_) => ThreadsScreen(
                          forumId: f['id'],
                          forumTitle: f['title'] ?? 'Forum',
                        ),
                      ),
                    );
                  },
                );
              },
            ),
    );
  }
}
