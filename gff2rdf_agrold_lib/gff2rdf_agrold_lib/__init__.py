#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
    This module annotate with the correct taxon and translate all gff file in RDF/ttl
    Run in two step, first call parser() fonction for build a dictionnary and second step
    call gff_model2rdf() for translate in ttl format

"""

__version__ = "0.0.1"

from agrold import gff_parser, gff_model2rdf
from prefix import *


