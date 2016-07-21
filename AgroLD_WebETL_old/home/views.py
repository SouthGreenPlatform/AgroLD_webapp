#-*- coding: utf-8 -*-
from django.http import HttpResponse
from datetime import datetime
from django.shortcuts import render

# Create your views here.

def home(request):
    return render(request, 'home/home.html', locals())