from rest_framework import viewsets
from rest_framework.decorators import action
from api import filters
from api.models import LoginApi
from api.serializers import LoginSerializer
from rest_framework.response import Response


# Create your views here.
class MainViewSet(viewsets.ViewSet):
    """base viewset for all requests."""
    fields = ()
    mandatory_fields = ()

    def get_params(self, request):
        """:returns request params"""
        params = {}
        for key in self.fields:
            val = request.query_params.getlist(key)
            if val:
                params[key] = val
        return params

    def list(self, request):
        """:returns all entries of the table"""
        params = self.get_params(request)
        missing_fields = [each for each in self.mandatory_fields if not params.get(each)]
        if missing_fields:
            ret_val = Response('Include missing params: %s' % missing_fields, 422)
        else:
            ret_val = Response(params)
        return ret_val


class LoginViewSet(viewsets.ModelViewSet):
    """fetches all the rows of data in login table"""
    queryset = LoginApi.objects.all()
    serializer_class = LoginSerializer
