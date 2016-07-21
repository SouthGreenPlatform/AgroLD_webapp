from django.shortcuts import render
from django.http import HttpResponse
from django.template import loader
from .models import Question

# Create your views here.


def index(request):
    latest_question_list = "Nordine"
    context = {'question': latest_question_list}
    return render(request, 'polls/index.html', context)

def detail(request, truc):
    return HttpResponse("You're looking at question %s." % truc)

def results(request, question_id):
    response = "You're looking at the results of question %s."
    return HttpResponse(response % question_id)

def vote(request, question_id):
    return HttpResponse("You're voting on question %s." % question_id)
