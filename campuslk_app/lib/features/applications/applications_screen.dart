import 'dart:convert';
import 'package:flutter/material.dart';
import '../../core/api/api_client.dart';

class ApplicationsScreen extends StatefulWidget {
  const ApplicationsScreen({super.key});

  @override
  State<ApplicationsScreen> createState() => _ApplicationsScreenState();
}

class _ApplicationsScreenState extends State<ApplicationsScreen> {
  bool loading = true;
  String? error;
  List applications = [];

  @override
  void initState() {
    super.initState();
    loadApplications();
  }

  // ✅ NOW this method exists
  Future<void> loadApplications() async {
    setState(() {
      loading = true;
      error = null;
    });

    try {
      final res = await ApiClient.get('/api/applications');

      if (!mounted) return;

      if (res.statusCode != 200) {
        String msg = "Failed to load (${res.statusCode})";
        try {
          msg = jsonDecode(res.body)['message'] ?? msg;
        } catch (_) {}

        setState(() {
          loading = false;
          error = msg;
          applications = [];
        });
        return;
      }

      final body = jsonDecode(res.body);
      setState(() {
        applications = body['data'] ?? [];
        loading = false;
      });
    } catch (e) {
      if (!mounted) return;
      setState(() {
        loading = false;
        error = "Network error: $e";
        applications = [];
      });
    }
  }

  Future<void> withdraw(String applicationId) async {
    try {
      // ✅ backend uses POST
      final res = await ApiClient.post('/api/applications/$applicationId/withdraw', {});

      if (!mounted) return;

      if (res.statusCode == 200) {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text("Application withdrawn")),
        );
        loadApplications(); // refresh
      } else {
        String msg = "Withdraw failed (${res.statusCode})";
        try {
          msg = jsonDecode(res.body)['message'] ?? msg;
        } catch (_) {}

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
      return const Scaffold(
        body: Center(child: CircularProgressIndicator()),
      );
    }

    if (error != null) {
      return Scaffold(
        body: Center(
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              Text(error!, textAlign: TextAlign.center),
              const SizedBox(height: 12),
              ElevatedButton(
                onPressed: loadApplications,
                child: const Text("Retry"),
              ),
            ],
          ),
        ),
      );
    }

    if (applications.isEmpty) {
      return const Scaffold(
        body: Center(child: Text("No applications yet")),
      );
    }

    return Scaffold(
      appBar: AppBar(title: const Text("My Applications")),
      body: RefreshIndicator(
        onRefresh: loadApplications,
        child: ListView.builder(
          itemCount: applications.length,
          itemBuilder: (context, i) {
            final a = applications[i];

            final id = (a['id'] ?? '').toString();
            final title = (a['opportunityTitle'] ?? 'Opportunity').toString();
            final status = (a['status'] ?? '-').toString();
            final submittedAt = (a['submittedAt'] ?? '').toString();

            final canWithdraw = status.toLowerCase() == "submitted";

            return Card(
              margin: const EdgeInsets.symmetric(horizontal: 12, vertical: 8),
              child: ListTile(
                title: Text(title),
                subtitle: Text("Status: $status\n$submittedAt"),
                trailing: (id.isNotEmpty && canWithdraw)
                    ? TextButton(
                        onPressed: () => withdraw(id),
                        child: const Text("Withdraw"),
                      )
                    : null,
              ),
            );
          },
        ),
      ),
    );
  }
}
