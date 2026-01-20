class CommentModel {
  final String id;
  final String content;
  final String createdAt;
  final String studentId;
  final String studentName;

  CommentModel({
    required this.id,
    required this.content,
    required this.createdAt,
    this.studentId = '',
    this.studentName = 'Unknown',
  });

  factory CommentModel.fromJson(Map<String, dynamic> json) {
    return CommentModel(
      id: json['id'] ?? '',
      content: json['content'] ?? '',
      createdAt: (json['createdAt'] ?? '').toString(),
      studentId: json['studentId'] ?? '',
      studentName: json['studentName'] ?? 'Unknown',
    );
  }
}
