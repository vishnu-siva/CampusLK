import 'package:shared_preferences/shared_preferences.dart';
import 'dart:developer' as dev;

class TokenStorage {
  static const String _tokenKey = 'auth_token';

  // ===============================
  // SAVE TOKEN
  // ===============================
  static Future<void> saveToken(String token) async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.setString(_tokenKey, token);

    // 🔍 DEBUG (for web)
    dev.log("TOKEN SAVED");
  }
  
 
  // ===============================
  // GET TOKEN
  // ===============================
  static Future<String?> getToken() async {
    final prefs = await SharedPreferences.getInstance();
    final t = prefs.getString(_tokenKey);

    // 🔍 DEBUG (for web)
    dev.log("TOKEN SAVED");

    return t;
  }

  // ===============================
  // CLEAR TOKEN (LOGOUT)
  // ===============================
  static Future<void> clearToken() async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.remove(_tokenKey);
  }
}
