from django.shortcuts import render
from django.template import RequestContext
from django.http import HttpResponseRedirect
from django.core.urlresolvers import reverse
from django.http import HttpResponse

from models import Document, transform, gffparser
from forms import DocumentForm

def detail(request, truc):
    return HttpResponse("You're looking at question %s." % truc)



def list(request):
    # Handle file upload
    if request.method == 'POST':
        form = DocumentForm(request.POST, request.FILES)
        if form.is_valid():
            newdoc = Document(docfile=request.FILES['docfile'])
            newdoc.save()

            # Redirect to the document list after POST
            return HttpResponseRedirect(reverse('list'))
    else:
        form = DocumentForm()  # A empty, unbound form

    # Load documents for the list page
    #documents = Document.objects.all()
    test = "toto"
    # Render list page with the documents and the form
    print "\n"
    #print  vars(documents)

    return render(
        request,
        'list.html',
        {'documents': documents, 'form': form}
    )


def gff(request):
    # Handle file upload
    if request.method == 'POST':
        form = DocumentForm(request.POST, request.FILES)
        if form.is_valid():
            newdoc = Document(docfile=request.FILES['docfile'])
            #newdoc.save()


            gffIstance = gffparser()
            retourGFF = gffIstance.parseGFF3(newdoc.docfile)



            return HttpResponseRedirect(reverse('gff'))
    else:
        form = DocumentForm()  # A empty, unbound form

    # Load documents for the list page
    documents = Document.objects.all()
    test = "toto"

    # Render list page with the documents and the form
    print "\n"
    #print vars(documents)
    #File = Document(docfile=request.FILES['docfile'])

    return render(request,'GFFToRDF.html' ,{'documents': documents, 'form': form, 'test':test})























