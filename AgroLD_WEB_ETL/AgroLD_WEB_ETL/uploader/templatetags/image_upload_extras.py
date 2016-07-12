from django import template
import os

register = template.Library()

# custom template tags: super heplful filters to minimze the javascript needed
#  https://docs.djangoproject.com/en/1.9/howto/custom-template-tags/#writing-custom-template-filters

# takes a local file_url and returns just the name.ext part at the end
@register.filter(name='parse_filename')
def parse_filename(docfile_name):
    return os.path.basename(docfile_name)
