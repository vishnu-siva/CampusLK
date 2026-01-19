// lib/features/opportunities/opportunity_detail_screen.dart
import 'dart:convert';
import 'package:flutter/material.dart';
import '../../core/api/api_client.dart';
import '../applications/apply_screen.dart';

class OpportunityDetailScreen extends StatefulWidget {
  final Map opportunity;
  const OpportunityDetailScreen({super.key, required this.opportunity});

  @override
  State<OpportunityDetailScreen> createState() => _OpportunityDetailScreenState();
}

class _OpportunityDetailScreenState extends State<OpportunityDetailScreen> {
  bool bookmarking = false;

  Future<void> bookmark() async {
    setState(() => bookmarking = true);

    final res = await ApiClient.post('/api/bookmarks/${widget.opportunity['id']}', {});
    String msg = "Bookmarked";
    try {
      msg = jsonDecode(res.body)['message'] ?? msg;
    } catch (_) {}

    if (!mounted) return;

    setState(() => bookmarking = false);

    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(content: Text(msg)),
    );
  }

  Future<void> openApply() async {
    final ok = await Navigator.push(
      context,
      MaterialPageRoute(
        builder: (_) => ApplyScreen(opportunity: Map<String, dynamic>.from(widget.opportunity)),
      ),
    );

    if (!mounted) return;

    if (ok == true) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text("Application submitted!")),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    final title = (widget.opportunity['title'] ?? 'Opportunity').toString();
    final desc = (widget.opportunity['description'] ?? '').toString();
    final type = (widget.opportunity['type'] ?? '').toString();
    final field = (widget.opportunity['fieldOfStudy'] ?? '').toString();
    final interests = (widget.opportunity['interests'] ?? '').toString();

    return Scaffold(
      appBar: AppBar(title: Text(title)),
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            if (type.isNotEmpty)
              Text(type, style: const TextStyle(fontWeight: FontWeight.bold)),
            const SizedBox(height: 12),
            Text(desc, style: const TextStyle(fontSize: 16)),
            const SizedBox(height: 18),

            if (field.isNotEmpty) Text("Field: $field"),
            if (interests.isNotEmpty) Text("Interests: $interests"),

            const SizedBox(height: 24),
            Row(
              children: [
                ElevatedButton(
                  onPressed: openApply,
                  child: const Text("Apply"),
                ),
                const SizedBox(width: 12),
                OutlinedButton(
                  onPressed: bookmarking ? null : bookmark,
                  child: Text(bookmarking ? "Saving..." : "Bookmark"),
                ),
              ],
            )
          ],
        ),
      ),
    );
  }
}
