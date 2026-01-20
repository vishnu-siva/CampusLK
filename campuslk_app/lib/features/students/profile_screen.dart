// lib/features/students/profile_screen.dart
import 'dart:convert';
import 'package:flutter/material.dart';
import '../../core/api/api_client.dart';
import '../../core/utils/api_response.dart';
import '../../core/storage/token_storage.dart';
import '../auth/login_screen.dart';
import '../../models/student.dart';
import 'edit_profile_screen.dart';

class ProfileScreen extends StatefulWidget {
  const ProfileScreen({super.key});

  @override
  State<ProfileScreen> createState() => _ProfileScreenState();
}

class _ProfileScreenState extends State<ProfileScreen> {
  Student? student;
  bool loading = true;

  Future<void> loadProfile() async {
    setState(() => loading = true);

    try {
      final res = await ApiClient.get('/api/students/me');

      // ✅ after any await, before using context or setState safely
      if (!mounted) return;

      if (res.statusCode != 200) {
        setState(() {
          loading = false;
          student = null;
        });

        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text("Profile load failed: ${res.statusCode}")),
        );
        return;
      }

      final json = jsonDecode(res.body);
      final api = ApiResponse.fromJson(json);

      setState(() {
        student = Student.fromJson(api.data);
        loading = false;
      });
    } catch (e) {
      if (!mounted) return;

      setState(() => loading = false);

      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text("Network error: $e")),
      );
    }
  }

  @override
  void initState() {
    super.initState();
    loadProfile();
  }

  @override
  Widget build(BuildContext context) {
    if (loading) {
      return const Scaffold(body: Center(child: CircularProgressIndicator()));
    }

    if (student == null) {
      return const Scaffold(body: Center(child: Text("Profile not found")));
    }

    return Scaffold(
      appBar: AppBar(
        title: const Text("My Profile"),
        actions: [
          IconButton(
            tooltip: 'Logout',
            icon: const Icon(Icons.logout),
            onPressed: () async {
              await TokenStorage.clearToken();
              if (!context.mounted) return;
              Navigator.pushAndRemoveUntil(
                context,
                MaterialPageRoute(builder: (_) => const LoginScreen()),
                (route) => false,
              );
            },
          ),
          IconButton(
            icon: const Icon(Icons.edit),
            onPressed: () async {
              await Navigator.push(
                context,
                MaterialPageRoute(
                  builder: (_) => EditProfileScreen(student: student!),
                ),
              );

              // ✅ guard context after await
              if (!context.mounted) return;

              loadProfile(); // refresh after edit
            },
          ),
          PopupMenuButton<String>(
            onSelected: (value) async {
              if (value == 'logout') {
                await TokenStorage.clearToken();
                if (!context.mounted) return;
                Navigator.pushAndRemoveUntil(
                  context,
                  MaterialPageRoute(builder: (_) => const LoginScreen()),
                  (route) => false,
                );
              }
            },
            itemBuilder: (context) => const [
              PopupMenuItem(
                value: 'logout',
                child: Text('Logout'),
              ),
            ],
          ),
        ],
      ),
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: Card(
          child: Padding(
            padding: const EdgeInsets.all(16),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  student!.name,
                  style: const TextStyle(
                    fontSize: 22,
                    fontWeight: FontWeight.bold,
                  ),
                ),
                const SizedBox(height: 6),
                Text(student!.email),
                const Divider(height: 24),
                Text("Field: ${student!.fieldOfStudy ?? '-'}"),
                Text("Interests: ${student!.interests ?? '-'}"),
                const SizedBox(height: 12),
                Text("Bio: ${student!.bio ?? '-'}"),
                Text("Portfolio: ${student!.portfolioUrl ?? '-'}"),
              ],
            ),
          ),
        ),
      ),
    );
  }
}
