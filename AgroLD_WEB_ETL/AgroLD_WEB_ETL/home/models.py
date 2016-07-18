from __future__ import unicode_literals

from django.db import models
import os, datetime
from django.utils.encoding import python_2_unicode_compatible

# Create your models here.
@python_2_unicode_compatible
class Blog(models.Model):
    pub_date = models.DateTimeField('date published', auto_now_add=True)
    author = models.CharField("author", max_length=25)
    title = models.CharField("title", max_length=140)
    text = models.TextField("text")

    def __str__(self):
        return "Blog Instance"

@python_2_unicode_compatible
class Comment(models.Model):
    pub_date = models.DateTimeField('date published')
    text = models.CharField("text", max_length=280)
    blog = models.ForeignKey(Blog)

    def __str__(self):
        return "Comment Instance"
