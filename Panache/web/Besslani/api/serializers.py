from api.models import LoginApi
from rest_framework.serializers import ModelSerializer


class LoginSerializer(ModelSerializer):
    class Meta:
        model = LoginApi
        fields = '__all__'
