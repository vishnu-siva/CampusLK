class Opportunity {
  final String id;
  final String title;
  final String description;
  final String type;
  final String? fieldOfStudy;
  final String? interests;

  Opportunity({
    required this.id,
    required this.title,
    required this.description,
    required this.type,
    this.fieldOfStudy,
    this.interests,
  });

  factory Opportunity.fromJson(Map<String, dynamic> json) {
    return Opportunity(
      id: json['id'] ?? '',
      title: json['title'] ?? '',
      description: json['description'] ?? '',
      type: json['type'] ?? '',
      fieldOfStudy: json['fieldOfStudy'],
      interests: json['interests'],
    );
  }
}
