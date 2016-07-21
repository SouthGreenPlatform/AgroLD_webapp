from django.conf.urls import url
from views import list

urlpatterns = [
    url(r'^list/$', list, name='list')
]
