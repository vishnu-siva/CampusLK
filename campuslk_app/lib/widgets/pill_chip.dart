import 'package:flutter/material.dart';

class PillChip extends StatelessWidget {
  final String label;
  final Color color;

  const PillChip({
    super.key,
    required this.label,
    this.color = const Color(0xFF5B2EFF),
  });

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 6),
      decoration: BoxDecoration(
        color: color.withValues(alpha: 0.1),
        borderRadius: BorderRadius.circular(20),
      ),
      child: Text(
        label,
        style: TextStyle(
          color: color,
          fontWeight: FontWeight.w600,
        ),
      ),
    );
  }
}
