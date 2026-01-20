class NotificationMessage {
  final String? id;
  final String? title;
  final String? body;
  final String? category;
  final String? targetStudentId;
  final bool read;
  final String? createdAt;

  NotificationMessage({
    this.id,
    this.title,
    this.body,
    this.category,
    this.targetStudentId,
    this.read = false,
    this.createdAt,
  });

  factory NotificationMessage.fromJson(Map<String, dynamic> json) {
    return NotificationMessage(
      id: json['id']?.toString(),
      title: json['title']?.toString(),
      body: json['body']?.toString(),
      category: json['category']?.toString(),
      targetStudentId: json['targetStudentId']?.toString(),
      read: json['read'] is bool ? json['read'] as bool : false,
      createdAt: json['createdAt']?.toString(),
    );
  }
}
