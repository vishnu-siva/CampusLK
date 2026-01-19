class Student {
  final String id;
  final String name;
  final String email;
  final String? bio;
  final String? portfolioUrl;
  final String? fieldOfStudy;
  final String? interests;

  Student({
    required this.id,
    required this.name,
    required this.email,
    this.bio,
    this.portfolioUrl,
    this.fieldOfStudy,
    this.interests,
  });

  factory Student.fromJson(Map<String, dynamic> json) {
    return Student(
      id: json['id'] ?? '',
      name: json['name'] ?? '',
      email: json['email'] ?? '',
      bio: json['bio'],
      portfolioUrl: json['portfolioUrl'],
      fieldOfStudy: json['fieldOfStudy'],
      interests: json['interests'],
    );
  }
}
