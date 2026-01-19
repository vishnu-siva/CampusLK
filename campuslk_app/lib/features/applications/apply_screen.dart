import 'dart:convert';
import 'package:flutter/material.dart';
import '../../core/api/api_client.dart';

class ApplyScreen extends StatefulWidget {
  final Map<String, dynamic> opportunity;

  const ApplyScreen({super.key, required this.opportunity});

  @override
  State<ApplyScreen> createState() => _ApplyScreenState();
}

class _ApplyScreenState extends State<ApplyScreen> {
  final _resumeUrl = TextEditingController();
  final _coverUrl = TextEditingController();
  final _notes = TextEditingController();

  bool sending = false;

  @override
  void dispose() {
    _resumeUrl.dispose();
    _coverUrl.dispose();
    _notes.dispose();
    super.dispose();
  }

  bool _isValidUrl(String s) {
    final v = s.trim();
    return v.startsWith("http://") || v.startsWith("https://");
  }

  Future<void> submit() async {
    final oppId = widget.opportunity['id']?.toString();
    if (oppId == null || oppId.isEmpty) {
      ScaffoldMessenger.of(context)
          .showSnackBar(const SnackBar(content: Text("Opportunity id missing")));
      return;
    }

    final resume = _resumeUrl.text.trim();
    if (resume.isEmpty || !_isValidUrl(resume)) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text("Please paste a valid Resume/CV URL (https://...)")),
      );
      return;
    }

    final cover = _coverUrl.text.trim();
    final notes = _notes.text.trim();

    setState(() => sending = true);

    try {
      // ✅ IMPORTANT: This is the correct payload that avoids opp=null
      final res = await ApiClient.post('/api/applications', {
        "opportunity": {"id": oppId},
        "resumeUrl": resume,
        "coverLetterUrl": cover.isEmpty ? "" : cover,
        "notes": notes.isEmpty ? "" : notes,

      });

      if (!mounted) return;

      setState(() => sending = false);

      String msg = "Applied";
      try {
        msg = jsonDecode(res.body)['message'] ?? msg;
      } catch (_) {}

      if (res.statusCode == 200 || res.statusCode == 201) {
        ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text(msg)));
        Navigator.pop(context, true); // return success
      } else {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text("Apply failed (${res.statusCode}): $msg")),
        );
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
    final title = widget.opportunity['title'] ?? 'Apply';

    return Scaffold(
      appBar: AppBar(title: Text("Apply - $title")),
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: ListView(
          children: [
            const Text(
              "Upload your CV/Report to Google Drive (or any cloud) and paste the share link here.\n"
              "Make sure the link is public: Anyone with the link can view.",
            ),
            const SizedBox(height: 16),

            TextField(
              controller: _resumeUrl,
              decoration: const InputDecoration(
                labelText: "Resume/CV URL (required)",
                hintText: "https://drive.google.com/...",
                border: OutlineInputBorder(),
              ),
            ),
            const SizedBox(height: 12),

            TextField(
              controller: _coverUrl,
              decoration: const InputDecoration(
                labelText: "Cover Letter URL (optional)",
                hintText: "https://drive.google.com/...",
                border: OutlineInputBorder(),
              ),
            ),
            const SizedBox(height: 12),

            TextField(
              controller: _notes,
              maxLines: 4,
              decoration: const InputDecoration(
                labelText: "Notes (optional)",
                hintText: "Why are you applying?",
                border: OutlineInputBorder(),
              ),
            ),
            const SizedBox(height: 16),

            ElevatedButton(
              onPressed: sending ? null : submit,
              child: sending
                  ? const SizedBox(
                      height: 20, width: 20,
                      child: CircularProgressIndicator(strokeWidth: 2),
                    )
                  : const Text("Submit Application"),
            ),
          ],
        ),
      ),
    );
  }
}
