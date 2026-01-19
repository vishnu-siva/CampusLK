import 'dart:convert';
import 'package:flutter/material.dart';
import '../../core/api/api_client.dart';
import 'create_thread_screen.dart';
import 'comments_screen.dart';

class ThreadsScreen extends StatefulWidget {
  final String forumId;
  final String forumTitle;

  const ThreadsScreen({super.key, required this.forumId, required this.forumTitle});

  @override
  State<ThreadsScreen> createState() => _ThreadsScreenState();
}

class _ThreadsScreenState extends State<ThreadsScreen> {
  bool loading = true;
  String? error;
  List threads = [];

  @override
  void initState() {
    super.initState();
    loadThreads();
  }

  Future<void> loadThreads() async {
    setState(() {
      loading = true;
      error = null;
    });

    try {
      final res = await ApiClient.get('/api/forums/${widget.forumId}/threads');
      if (!mounted) return;

      if (res.statusCode != 200) {
        String msg = "Failed (${res.statusCode})";
        try {
          msg = jsonDecode(res.body)['message'] ?? msg;
        } catch (_) {}
        setState(() {
          loading = false;
          error = msg;
          threads = [];
        });
        return;
      }

      final body = jsonDecode(res.body);
      setState(() {
        threads = body['data'] ?? [];
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

  Future<void> openCreateThread() async {
    final created = await Navigator.push(
      context,
      MaterialPageRoute(
        builder: (_) => CreateThreadScreen(forumId: widget.forumId),
      ),
    );

    if (!mounted) return;
    if (created == true) {
      loadThreads();
    }
  }

  @override
  Widget build(BuildContext context) {
    if (loading) {
      return Scaffold(
        appBar: AppBar(title: Text("Threads: ${widget.forumTitle}")),
        body: const Center(child: CircularProgressIndicator()),
      );
    }

    if (error != null) {
      return Scaffold(
        appBar: AppBar(title: Text("Threads: ${widget.forumTitle}")),
        body: Center(
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              Text(error!, textAlign: TextAlign.center),
              const SizedBox(height: 12),
              ElevatedButton(onPressed: loadThreads, child: const Text("Retry")),
            ],
          ),
        ),
        floatingActionButton: FloatingActionButton(
          onPressed: openCreateThread,
          child: const Icon(Icons.add),
        ),
      );
    }

    return Scaffold(
      appBar: AppBar(title: Text("Threads: ${widget.forumTitle}")),
      body: threads.isEmpty
          ? const Center(child: Text("No threads yet. Tap + to create one."))
          : ListView.builder(
              itemCount: threads.length,
              itemBuilder: (context, i) {
                final t = threads[i];
                return ListTile(
                  title: Text(t['title'] ?? ''),
                  subtitle: Text("By: ${t['createdBy'] ?? '-'}"),
                  trailing: const Icon(Icons.chevron_right),
                  onTap: () {
                    Navigator.push(
                      context,
                      MaterialPageRoute(
                        builder: (_) => CommentsScreen(
                          threadId: t['id'],
                          threadTitle: t['title'] ?? 'Thread',
                        ),
                      ),
                    );
                  },
                );
              },
            ),
      floatingActionButton: FloatingActionButton(
        onPressed: openCreateThread,
        child: const Icon(Icons.add),
      ),
    );
  }
}
