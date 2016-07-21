
from django.conf.urls import patterns, url

urlpatterns = patterns('home.views',
    url(r'^home$', 'home')

)