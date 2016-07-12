from django.shortcuts import render
from django.template import RequestContext
from django.http import HttpResponseRedirect, HttpResponseNotFound
from django.core.urlresolvers import reverse

from models import Blog, Comment
from forms import BlogForm



def home(request):
    return render(request, 'home/home.html', locals())


def gff(request):
    return render(request, 'home/home.html', locals())


