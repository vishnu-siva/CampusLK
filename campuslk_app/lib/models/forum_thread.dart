class ForumThread {
  final String id;
  final String title;
  final String createdBy;
  final String createdByName;

  ForumThread({
    required this.id,
    required this.title,
    this.createdBy = '',
    this.createdByName = 'Unknown',
  });

  factory ForumThread.fromJson(Map<String, dynamic> json) {
    return ForumThread(
      id: json['id'] ?? '',
      title: json['title'] ?? '',
      createdBy: json['createdBy'] ?? '',
      createdByName: json['createdByName'] ?? json['studentName'] ?? 'Unknown',
    );
  }
}
