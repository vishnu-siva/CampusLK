import 'package:flutter/material.dart';
import 'theme/app_theme.dart';
import 'features/auth/login_screen.dart';

void main() {
  runApp(const CampusLKApp());
}

class CampusLKApp extends StatelessWidget {
  const CampusLKApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'CampusLK',
      debugShowCheckedModeBanner: false,
      theme: AppTheme.lightTheme,
      home: const LoginScreen(),
    );
  }
}
