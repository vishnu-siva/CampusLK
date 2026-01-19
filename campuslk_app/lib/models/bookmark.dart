class Bookmark {
  final String id;
  final String opportunityId;
  final String opportunityTitle;
  final String createdAt;

  Bookmark({
    required this.id,
    required this.opportunityId,
    required this.opportunityTitle,
    required this.createdAt,
  });

  factory Bookmark.fromJson(Map<String, dynamic> json) {
    return Bookmark(
      id: json['id'] ?? '',
      opportunityId: json['opportunityId'] ?? '',
      opportunityTitle: json['opportunityTitle'] ?? '',
      createdAt: (json['createdAt'] ?? '').toString(),
    );
  }
}
