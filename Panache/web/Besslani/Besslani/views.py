import logging
from rest_framework import exceptions
from rest_framework.renderers import CoreJSONRenderer
from rest_framework.response import Response
from rest_framework.schemas import SchemaGenerator
from rest_framework.views import APIView
from rest_framework_swagger import renderers

from Besslani.conf.permission import ApiPermission

logger = logging.getLogger(__name__)


class SwaggerSchemaView(APIView):
    """Generate swagger schema."""
    _ignore_model_permissions = True
    exclude_from_schema = True
    permission_classes = [ApiPermission]
    renderer_classes = [
        CoreJSONRenderer,
        renderers.OpenAPIRenderer,
        renderers.SwaggerUIRenderer
    ]

    def get(self, request):
        """."""
        generator = SchemaGenerator()
        schema = generator.get_schema(request=request)
        if not schema:
            raise exceptions.ValidationError(
                'The schema generator did not return a schema Document'
            )
        return Response(schema)


