import 'package:flutter/material.dart';
import '../../core/api/api_client.dart';

class CreateThreadScreen extends StatefulWidget {
  final String forumId;
  const CreateThreadScreen({super.key, required this.forumId});

  @override
  State<CreateThreadScreen> createState() => _CreateThreadScreenState();
}

class _CreateThreadScreenState extends State<CreateThreadScreen> {
  final titleCtrl = TextEditingController();
  final bodyCtrl = TextEditingController();
  bool loading = false;

  Future<void> createThread() async {
    if (titleCtrl.text.isEmpty || bodyCtrl.text.isEmpty) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text("Title and content required")),
      );
      return;
    }

    setState(() => loading = true);

    final res = await ApiClient.post(
      '/api/forums/${widget.forumId}/threads',
      {
        "title": titleCtrl.text,
        "content": bodyCtrl.text,
      },
    );

    setState(() => loading = false);

    if (!mounted) return;

    if (res.statusCode == 200 || res.statusCode == 201) {
      Navigator.pop(context);
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text("Thread created")),
      );
    } else {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text("Failed: ${res.body}")),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text("Create Thread")),
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          children: [
            TextField(
              controller: titleCtrl,
              decoration: const InputDecoration(labelText: "Thread title"),
            ),
            const SizedBox(height: 12),
            TextField(
              controller: bodyCtrl,
              decoration: const InputDecoration(labelText: "Thread content"),
              maxLines: 6,
            ),
            const Spacer(),
            SizedBox(
              width: double.infinity,
              child: ElevatedButton(
                onPressed: loading ? null : createThread,
                child: Text(loading ? "Creating..." : "Create"),
              ),
            )
          ],
        ),
      ),
    );
  }
}
