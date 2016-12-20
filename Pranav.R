setwd("C:\\Users\\Rhys-Pc\\Desktop")
data<-read.csv("Stats.csv", head=TRUE)
data2<-cbind(data[,c(1,7,8,14,15)])
# H. Expected goals = Home attack strength * Away def strengh * ave goals home (always 1.21)

HomeAttk<-matrix(,nrow=20, ncol=20)
AwayAttk<-matrix(,nrow=20, ncol=20)

for (i in 1:20){
  for (j in 1:20){
    HomeAttk[i,j]= data2[i,2]*data2[j,5]*1.21
    AwayAttk[i,j]= data[j,2]*data2[i,5]*1.19
  }
}

diag(HomeAttk)<-0
diag(AwayAttk)<-0

Pois<-matrix(, nrow=8, ncol=8)

for (i in 0:7){
  for (j in 0:7){
    Pois[i,j]= (dpois(i,lambda=HomeAttk[1,3])*dpois(j,lambda=AwayAttk[1,3]))*100
  }
}


ppois(1:10,lambda=HomeAttk,lower=FALSE)
