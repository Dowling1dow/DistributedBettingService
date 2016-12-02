from flask import Flask
from flask import render_template, request
import urllib2, json

fixtures_url = "http://localhost:8182/fixtures"

app = Flask(__name__, static_url_path='/static')

@app.route('/', methods=['GET', 'POST'])
def index():
	if request.method=='GET':
		return render_template('home.html')
	elif request.method=='POST':
		text = request.form['text']
		processed_text = text.upper()
		print processed_text
		return render_template('home.html', team_name=processed_text)
	else:
		return render_template('home.html')

	

@app.route('/info', methods=['GET', 'POST'])
def info():
	if request.method=='GET':
		response = urllib2.urlopen(fixtures_url)
		# json_raw = response.readlines()
		json_object = json.load(response)
		# print json_object['Fixtures'][0]
		fixtures = []
		for match in json_object['Fixtures']:
			match_string = match['HomeTeam'].replace(" ", "") +"vs"+ match['AwayTeam'].replace(" ", "")
			fixtures.append(match_string)

		return render_template('info.html', fixtures=fixtures)
		
	elif request.method=='POST':
		text = request.form['text']
		processed_text = text.upper()
		print processed_text

		return processed_text
	
	else:
		return("ok")

# @app.route("/info",  methods=['POST'])
# def info_team(team_name):
	
	# return 'You picked '+str(team_name)