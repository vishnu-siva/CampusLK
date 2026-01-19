import 'dart:convert';
import 'package:flutter/material.dart';
import '../../core/api/api_client.dart';
import 'opportunity_detail_screen.dart';
import 'add_opportunity_screen.dart';

class OpportunityFeedScreen extends StatefulWidget {
  const OpportunityFeedScreen({super.key});

  @override
  State<OpportunityFeedScreen> createState() =>
      _OpportunityFeedScreenState();
}

class _OpportunityFeedScreenState
    extends State<OpportunityFeedScreen> {
  List opportunities = [];
  bool loading = true;

  @override
  void initState() {
    super.initState();
    loadOpportunities();
  }

  Future<void> loadOpportunities() async {
    final res = await ApiClient.get('/api/opportunities');
    final body = jsonDecode(res.body);

    setState(() {
      opportunities = body['data'] ?? [];
      loading = false;
    });
  }

  @override
  Widget build(BuildContext context) {
    if (loading) {
      return const Center(child: CircularProgressIndicator());
    }

    if (opportunities.isEmpty) {
      return const Center(child: Text("No opportunities yet"));
    }

    return Scaffold(
      body: ListView.builder(
        itemCount: opportunities.length,
        itemBuilder: (context, index) {
          final o = opportunities[index];

          return Card(
            margin: const EdgeInsets.symmetric(
              horizontal: 12,
              vertical: 6,
            ),
            child: ListTile(
              title: Text(o['title'] ?? ''),
              subtitle: Text(o['description'] ?? ''),
              trailing: const Icon(Icons.arrow_forward_ios),

              // ✅ THIS IS THE IMPORTANT PART
              onTap: () {
                Navigator.push(
                  context,
                  MaterialPageRoute(
                    builder: (_) =>
                        OpportunityDetailScreen(opportunity: o),
                  ),
                );
              },
            ),
          );
        },
      ),

      // ➕ Button to add new opportunity
      floatingActionButton: FloatingActionButton(
        onPressed: () async {
          await Navigator.push(
            context,
            MaterialPageRoute(
              builder: (_) => const AddOpportunityScreen(),
            ),
          );
          loadOpportunities(); // refresh feed
        },
        child: const Icon(Icons.add),
      ),
    );
  }
}
