from django.db import models


# Create your models here.
class LoginApi(models.Model):
    username = models.CharField(max_length=255, primary_key=True, null=False)
    password = models.CharField(max_length=255, null=False)
    role = models.CharField(max_length=255, null=False)

    class Meta:
        db_table = 'customer_details'
