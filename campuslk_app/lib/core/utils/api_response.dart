class ApiResponse<T> {
  final bool success;
  final String message;
  final T data;

  ApiResponse({
    required this.success,
    required this.message,
    required this.data,
  });

  factory ApiResponse.fromJson(Map<String, dynamic> json) {
    return ApiResponse(
      success: json['success'] ?? true,
      message: json['message'] ?? '',
      data: json['data'],
    );
  }
}
