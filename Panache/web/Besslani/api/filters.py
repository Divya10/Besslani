import logging
from api.models import LoginApi
import coreapi
# from rest_framework import filters
from django_filters import rest_framework as filters

logger = logging.getLogger(__name__)


# class BesslaniFilter(filters.BaseFilterBackend):
#     """Base filter class for Besslani end points."""
#     fields = ()
#
#     def get_schema_fields(self, view):
#         return [coreapi.Field(name=f, location="query", required=False, type="list")
#                 for f in self.fields]
#
#     def filter_queryset(self, request, queryset, view):
#         return queryset


# Define filters from Pcf Filter
class LoginFilter(filters.FilterSet):
    """Filters for api end point."""
    # model = LoginApi
    # mandatory_fields = ('user',)
    # optional_fields = ('role',)
    # fields = mandatory_fields + optional_fields
    """
    Use isnull filter in exclude mode to determine active/deleted instances.
    Use isnull filter in exclude mode to determine spl01/spl02 instances.
    """
    deleted = filters.BooleanFilter(field_name="deleted", lookup_expr='isnull',
                                    exclude=True)
    imported = filters.BooleanFilter(field_name="imported",
                                     lookup_expr='isnull', exclude=True)

    class Meta:
        model = LoginApi
        fields = ('username', 'password', 'role')
