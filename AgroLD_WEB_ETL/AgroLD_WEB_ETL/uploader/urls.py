from django.conf.urls import url
from views import list, gff, detail

urlpatterns = [
    url(r'^list/$', list, name='list'),
    url(r'^gff', gff, name='gff'),
    url(r'^(?P<truc>[A-Za-z0-9]+)/$', detail, name='detail'),
    #url(r'^post/(?P<pk>[0-9]+)/$', post_detail, name='post_detail'),

]
