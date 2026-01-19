import 'dart:convert';
import 'package:flutter/material.dart';
import '../../core/api/api_client.dart';
import '../../models/project.dart';

class SendRequestScreen extends StatefulWidget {
  final Project project;
  const SendRequestScreen({super.key, required this.project});

  @override
  State<SendRequestScreen> createState() => _SendRequestScreenState();
}

class _SendRequestScreenState extends State<SendRequestScreen> {
  final toStudentId = TextEditingController();
  bool sending = false;

  Future<void> send() async {
    setState(() => sending = true);

    final res = await ApiClient.post('/api/collaboration-requests', {
      "toStudent": {"id": toStudentId.text.trim()},
      "project": {"id": widget.project.id},
      "status": "pending"
    });

    setState(() => sending = false);

      if (!mounted) return;

      if (res.statusCode == 200) {
        ScaffoldMessenger.of(context)
            .showSnackBar(const SnackBar(content: Text("Request sent")));
        Navigator.pop(context);
      } else {
        final msg = jsonDecode(res.body)['message'] ?? "Failed";
        ScaffoldMessenger.of(context)
            .showSnackBar(SnackBar(content: Text(msg)));
      }

  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text("Send Request")),
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          children: [
            const Text("Enter the studentId of the person you want to invite:"),
            const SizedBox(height: 10),
            TextField(controller: toStudentId, decoration: const InputDecoration(labelText: "To Student ID")),
            const SizedBox(height: 16),
            ElevatedButton(
              onPressed: sending ? null : send,
              child: Text(sending ? "Sending..." : "Send"),
            )
          ],
        ),
      ),
    );
  }
}
