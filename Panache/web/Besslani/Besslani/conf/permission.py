"""Define Api end point permissions."""
from rest_framework import permissions


class ApiPermission(permissions.BasePermission):
    """Allow GET requests from authenticated users."""

    def has_permission(self, request, view):
        return bool(
            (request.method == "GET" or request.method == "DELETE" or request.method == "POST") and
            request.user and
            request.user.is_authenticated
        )
