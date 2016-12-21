import urllib2, json, csv, re
import numpy as math

def poisson_dist(x,mean):
    p = math.exp(-mean)
    for i in xrange(x):
        p *= mean
        p /= i + 1
    return p


def predict(home_attacking_strength,home_defesnive_strength,away_attacking_strength,away_defensive_strength):
    #Trial using ManchesterUnited as Home team and Hull as Away
    #Home team attacking strength * Away Team Defensive Strength * Average Goals Home 1.64
    home_team_goal_expectancy =home_attacking_strength*away_defensive_strength*1.64
    # Away team attacking strength * Home Team Defensive Strength * Average Goals Away 1.19
    away_team_goal_expectancy = away_attacking_strength*home_defesnive_strength*1.19
    score={}
    for i in range(0,8):
        for j in range (0,8):
            score[i,j]=(poisson_dist(i,home_team_goal_expectancy)*poisson_dist(j,away_team_goal_expectancy))*100
            score[i,j]=round(score[i,j],2)

    home_win=0;
    away_win=0;
    draw=0;
    for i in range(0,8):
        for j in range(0,8):
           if(i==j):
               draw=draw+score[i,j]
           elif (i<j):
                away_win=away_win+score[i,j]
           else:
               home_win=home_win+score[i,j]

    print 'Probability of draw is '+str(draw)+'%'
    print 'Probability of home win is '+str(home_win)+'%'
    print 'Probability of away win is '+str(away_win)+'%'


#Tests
#Arsenal vs Chelsea
predict(1.6,0.42,2.1,0.69)

#Manchester United vs Burnley
predict(0.84,0.63,0.21,1.38)
