from flask import Flask
from flask import render_template
from app import app

app = Flask(__name__)

@app.route('/')
def index():
	return render_template('home.html', name=name)
	# return 'Home page'

@app.route('/info')
def info():
	return 'Info page'