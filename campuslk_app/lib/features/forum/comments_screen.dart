import 'dart:convert';
import 'package:flutter/material.dart';
import '../../core/api/api_client.dart';

class CommentsScreen extends StatefulWidget {
  final String threadId;
  final String threadTitle;

  const CommentsScreen({super.key, required this.threadId, required this.threadTitle});

  @override
  State<CommentsScreen> createState() => _CommentsScreenState();
}

class _CommentsScreenState extends State<CommentsScreen> {
  bool loading = true;
  String? error;
  List comments = [];
  final commentCtrl = TextEditingController();
  bool sending = false;

  @override
  void initState() {
    super.initState();
    loadComments();
  }

  @override
  void dispose() {
    commentCtrl.dispose();
    super.dispose();
  }

  Future<void> loadComments() async {
    setState(() {
      loading = true;
      error = null;
    });

    try {
      final res = await ApiClient.get('/api/threads/${widget.threadId}/comments');
      if (!mounted) return;

      if (res.statusCode != 200) {
        String msg = "Failed (${res.statusCode})";
        try {
          msg = jsonDecode(res.body)['message'] ?? msg;
        } catch (_) {}
        setState(() {
          loading = false;
          error = msg;
          comments = [];
        });
        return;
      }

      final body = jsonDecode(res.body);
      setState(() {
        comments = body['data'] ?? [];
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

  Future<void> addComment() async {
    final text = commentCtrl.text.trim();
    if (text.isEmpty) return;

    setState(() => sending = true);

    try {
      final res = await ApiClient.post('/api/threads/${widget.threadId}/comments', {
        "content": text
      });

      if (!mounted) return;
      setState(() => sending = false);

      if (res.statusCode == 200) {
        commentCtrl.clear();
        loadComments();
      } else {
        String msg = "Failed (${res.statusCode})";
        try {
          msg = jsonDecode(res.body)['message'] ?? msg;
        } catch (_) {}
        ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text(msg)));
      }
    } catch (e) {
      if (!mounted) return;
      setState(() => sending = false);
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text("Network error: $e")),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    if (loading) {
      return Scaffold(
        appBar: AppBar(title: Text(widget.threadTitle)),
        body: const Center(child: CircularProgressIndicator()),
      );
    }

    if (error != null) {
      return Scaffold(
        appBar: AppBar(title: Text(widget.threadTitle)),
        body: Center(
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              Text(error!, textAlign: TextAlign.center),
              const SizedBox(height: 12),
              ElevatedButton(onPressed: loadComments, child: const Text("Retry")),
            ],
          ),
        ),
      );
    }

    return Scaffold(
      appBar: AppBar(title: Text(widget.threadTitle)),
      body: Column(
        children: [
          Expanded(
            child: comments.isEmpty
                ? const Center(child: Text("No comments yet"))
                : ListView.builder(
                    itemCount: comments.length,
                    itemBuilder: (context, i) {
                      final c = comments[i];
                      final authorName = c['studentName'] ?? 'Unknown';
                      return ListTile(
                        title: Text(c['content'] ?? ''),
                        subtitle: Text("By: $authorName"),
                      );
                    },
                  ),
          ),
          Padding(
            padding: const EdgeInsets.all(12),
            child: Row(
              children: [
                Expanded(
                  child: TextField(
                    controller: commentCtrl,
                    decoration: const InputDecoration(
                      hintText: "Write a comment...",
                      border: OutlineInputBorder(),
                    ),
                  ),
                ),
                const SizedBox(width: 8),
                ElevatedButton(
                  onPressed: sending ? null : addComment,
                  child: Text(sending ? "..." : "Send"),
                )
              ],
            ),
          )
        ],
      ),
    );
  }
}
