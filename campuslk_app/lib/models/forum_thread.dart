class ForumThread {
  final String id;
  final String title;

  ForumThread({required this.id, required this.title});

  factory ForumThread.fromJson(Map<String, dynamic> json) {
    return ForumThread(
      id: json['id'] ?? '',
      title: json['title'] ?? '',
    );
  }
}
