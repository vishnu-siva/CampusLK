import 'dart:convert';
import 'package:flutter/material.dart';
import '../../core/api/api_client.dart';

class AddOpportunityScreen extends StatefulWidget {
  const AddOpportunityScreen({super.key});

  @override
  State<AddOpportunityScreen> createState() => _AddOpportunityScreenState();
}

class _AddOpportunityScreenState extends State<AddOpportunityScreen> {
  final title = TextEditingController();
  final description = TextEditingController();
  final type = TextEditingController(text: "internship");
  final field = TextEditingController(text: "CS");
  final interests = TextEditingController(text: "AI,Flutter");

  bool saving = false;

  Future<void> save() async {
    setState(() => saving = true);

    final res = await ApiClient.post('/api/opportunities', {
      "title": title.text.trim(),
      "description": description.text.trim(),
      "type": type.text.trim(),
      "fieldOfStudy": field.text.trim(),
      "interests": interests.text.trim(),
    });

    setState(() => saving = false);

    if (res.statusCode == 200) {
      if (!mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text("Opportunity created ✅")),
      );
      Navigator.pop(context);
    } else {
      String msg = "Create failed (${res.statusCode})";
      try {
        msg = jsonDecode(res.body)['message'] ?? msg;
      } catch (_) {}
      if (!mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text(msg)),
      );
    }
  }

  @override
  void dispose() {
    title.dispose();
    description.dispose();
    type.dispose();
    field.dispose();
    interests.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text("Add Opportunity")),
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: ListView(
          children: [
            TextField(controller: title, decoration: const InputDecoration(labelText: "Title")),
            const SizedBox(height: 10),
            TextField(
              controller: description,
              maxLines: 4,
              decoration: const InputDecoration(labelText: "Description"),
            ),
            const SizedBox(height: 10),
            TextField(controller: type, decoration: const InputDecoration(labelText: "Type (internship/scholarship/etc)")),
            const SizedBox(height: 10),
            TextField(controller: field, decoration: const InputDecoration(labelText: "Field of Study")),
            const SizedBox(height: 10),
            TextField(controller: interests, decoration: const InputDecoration(labelText: "Interests (comma separated)")),
            const SizedBox(height: 16),
            ElevatedButton(
              onPressed: saving ? null : save,
              child: Text(saving ? "Saving..." : "Create"),
            )
          ],
        ),
      ),
    );
  }
}
