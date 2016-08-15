D=[1:2000];
R=[1:100];

MIN=zeros(2,length(D));

for dId=1:length(D)
n=D(dId);
totalEvals=[];
for r=R
p=1/2+erf(sqrt(r)/2)/2;
%p=erf(sqrt(r));
E=zeros(1,n);
E(1)=1/p;
for i=1:n-1
E(i+1) = n/(n-i)/p + i/(n-i)/p*(1-p)*E(i);
% if(E(i+1)>1000000)
%     break
% end
end
totalEvals=[totalEvals sum(E)*2*r];
end
[MIN(2,dId),MIN(1,dId)]=min(totalEvals);
total=[total; totalEvals];
end
figure
plot(D,R(MIN(1,:)),'b','Linewidth',2);

ylabel('Optimal resampling number','FontSize',16);
xlabel('Problem dimension','FontSize',16);
set(gca,'FontSize',16)