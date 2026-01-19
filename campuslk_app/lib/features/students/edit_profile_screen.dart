import 'dart:convert';
import 'package:flutter/material.dart';
import '../../core/api/api_client.dart';
import '../../models/student.dart';

class EditProfileScreen extends StatefulWidget {
  final Student student;
  const EditProfileScreen({super.key, required this.student});

  @override
  State<EditProfileScreen> createState() => _EditProfileScreenState();
}

class _EditProfileScreenState extends State<EditProfileScreen> {
  late final TextEditingController name;
  late final TextEditingController bio;
  late final TextEditingController portfolio;
  late final TextEditingController field;
  late final TextEditingController interests;

  bool saving = false;

  @override
  void initState() {
    super.initState();
    name = TextEditingController(text: widget.student.name);
    bio = TextEditingController(text: widget.student.bio ?? "");
    portfolio = TextEditingController(text: widget.student.portfolioUrl ?? "");
    field = TextEditingController(text: widget.student.fieldOfStudy ?? "");
    interests = TextEditingController(text: widget.student.interests ?? "");
  }

  Future<void> save() async {
    setState(() => saving = true);

    final res = await ApiClient.put('/api/students/me', {
      "name": name.text,
      "bio": bio.text,
      "portfolioUrl": portfolio.text,
      "fieldOfStudy": field.text,
      "interests": interests.text,
    });

    setState(() => saving = false);

  if (!mounted) return;

  if (res.statusCode == 200) {
    Navigator.pop(context);
  } else {
    final msg = jsonDecode(res.body)['message'] ?? 'Update failed';
    ScaffoldMessenger.of(context)
        .showSnackBar(SnackBar(content: Text(msg)));
  }

  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text("Edit Profile")),
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: ListView(
          children: [
            TextField(controller: name, decoration: const InputDecoration(labelText: "Name")),
            TextField(controller: field, decoration: const InputDecoration(labelText: "Field of Study")),
            TextField(controller: interests, decoration: const InputDecoration(labelText: "Interests")),
            TextField(controller: bio, decoration: const InputDecoration(labelText: "Bio")),
            TextField(controller: portfolio, decoration: const InputDecoration(labelText: "Portfolio URL")),
            const SizedBox(height: 16),
            ElevatedButton(
              onPressed: saving ? null : save,
              child: Text(saving ? "Saving..." : "Save"),
            ),
          ],
        ),
      ),
    );
  }
}
