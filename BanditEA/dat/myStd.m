% mean by column
function [myMean, myStd]=myStd(M)
[m,n]=size(M);
myMean=zeros(1,n);
myStd=zeros(1,n);
for i=1:n
    idx=find(M(:,i)>0);
    myMean(i)=mean(M(idx,i));
    myStd(i)=std(M(idx,i))/sqrt(length(idx));
end
end