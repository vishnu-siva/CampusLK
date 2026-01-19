class Forum {
  final String id;
  final String title;
  final String? description;

  Forum({required this.id, required this.title, this.description});

  factory Forum.fromJson(Map<String, dynamic> json) {
    return Forum(
      id: json['id'] ?? '',
      title: json['title'] ?? '',
      description: json['description'],
    );
  }
}
