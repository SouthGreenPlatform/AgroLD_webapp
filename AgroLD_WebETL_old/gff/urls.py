from django.conf.urls import patterns, url

urlpatterns = patterns('gff.views',
    url(r'^gff$', 'gff'),
    url(r'^list/$', list, name='list')

)