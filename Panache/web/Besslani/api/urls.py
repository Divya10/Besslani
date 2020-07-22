"""rest endpoints for login api"""
from rest_framework import routers

from api import views

app_name = 'api'

router = routers.DefaultRouter()
router.register(r'/login', views.LoginViewSet, base_name='user database')

urlpatterns = router.urls
