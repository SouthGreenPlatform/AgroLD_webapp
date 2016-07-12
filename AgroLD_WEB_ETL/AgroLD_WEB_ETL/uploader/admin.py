from django.contrib import admin

from AgroLD_WEB_ETL.uploader.models import Document
admin.site.register(Document)

from AgroLD_WEB_ETL.home.models import Blog, Comment
admin.site.register(Blog)
admin.site.register(Comment)
