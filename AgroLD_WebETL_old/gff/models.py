#-*- coding: utf-8 -*-
from __future__ import unicode_literals
from django.shortcuts import render
from django.db import models

# Create your models here.

import os

# Create your models here.

class Document(models.Model):
    docfile = models.FileField(upload_to='documents/%Y/%m/%d')
