// lib/features/bookmarks/bookmarks_screen.dart
import 'dart:convert';
import 'package:flutter/material.dart';
import '../../core/api/api_client.dart';

class BookmarksScreen extends StatefulWidget {
  const BookmarksScreen({super.key});

  @override
  State<BookmarksScreen> createState() => _BookmarksScreenState();
}

class _BookmarksScreenState extends State<BookmarksScreen> {
  bool loading = true;
  List bookmarks = [];
  String? error;

  @override
  void initState() {
    super.initState();
    loadBookmarks();
  }

  Future<void> loadBookmarks() async {
    setState(() {
      loading = true;
      error = null;
    });

    try {
      final res = await ApiClient.get('/api/bookmarks');

      if (res.statusCode != 200) {
        String msg = "Failed to load bookmarks (${res.statusCode})";
        try {
          msg = jsonDecode(res.body)['message'] ?? msg;
        } catch (_) {}
        setState(() {
          loading = false;
          error = msg;
          bookmarks = [];
        });
        return;
      }

      final body = jsonDecode(res.body);
      setState(() {
        bookmarks = body['data'] ?? [];
        loading = false;
      });
    } catch (e) {
      setState(() {
        loading = false;
        error = "Network error: $e";
        bookmarks = [];
      });
    }
  }

  Future<void> removeBookmark(String opportunityId) async {
    try {
      final res = await ApiClient.delete('/api/bookmarks/$opportunityId');

      if (res.statusCode == 200) {
        // refresh list
        await loadBookmarks();
        if (!mounted) return;
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text("Bookmark removed")),
        );
      } else {
        String msg = "Remove failed (${res.statusCode})";
        try {
          msg = jsonDecode(res.body)['message'] ?? msg;
        } catch (_) {}
        if (!mounted) return;
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text(msg)),
        );
      }
    } catch (e) {
      if (!mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text("Network error: $e")),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    if (loading) {
      return const Center(child: CircularProgressIndicator());
    }

    if (error != null) {
      return Center(
        child: Padding(
          padding: const EdgeInsets.all(16),
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              Text(error!, textAlign: TextAlign.center),
              const SizedBox(height: 12),
              ElevatedButton(
                onPressed: loadBookmarks,
                child: const Text("Retry"),
              )
            ],
          ),
        ),
      );
    }

    if (bookmarks.isEmpty) {
      return const Center(child: Text("No bookmarks yet"));
    }

    // Expected backend DTO per item:
    // { id, opportunityId, opportunityTitle, createdAt }
    return RefreshIndicator(
      onRefresh: loadBookmarks,
      child: ListView.builder(
        itemCount: bookmarks.length,
        itemBuilder: (context, index) {
          final b = bookmarks[index];

          final opportunityId = (b['opportunityId'] ?? '').toString();
          final title = (b['opportunityTitle'] ?? '').toString();
          final createdAt = (b['createdAt'] ?? '').toString();

          return Card(
            margin: const EdgeInsets.symmetric(horizontal: 12, vertical: 6),
            child: ListTile(
              title: Text(title.isEmpty ? "Saved Opportunity" : title),
              subtitle: createdAt.isEmpty ? null : Text("Saved at: $createdAt"),
              trailing: opportunityId.isEmpty
                  ? null
                  : IconButton(
                      icon: const Icon(Icons.delete_outline),
                      tooltip: "Remove bookmark",
                      onPressed: () => removeBookmark(opportunityId),
                    ),
            ),
          );
        },
      ),
    );
  }
}
