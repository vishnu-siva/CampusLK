class CommentModel {
  final String id;
  final String content;
  final String createdAt;

  CommentModel({required this.id, required this.content, required this.createdAt});

  factory CommentModel.fromJson(Map<String, dynamic> json) {
    return CommentModel(
      id: json['id'] ?? '',
      content: json['content'] ?? '',
      createdAt: (json['createdAt'] ?? '').toString(),
    );
  }
}
