function E=rmhcEvalsHist(n,r)
%disp(sprintf('dimension=%d, resamples=%d, p=%f',n,r,pc));
E=[];
for test=1:100
    evals=0;
    i=0;
    %i=randi(n+1,1)-1;
    hr=0;
    while(i<n && evals<=10000000 )
        p=rand;
        if(p>=i/n)
            p1=randn/sqrt(r+hr);
            %p1=randn/sqrt(r);
            p2=randn/sqrt(r);
            if(p2+1>=p1)
                i=i+1;
                hr=0;
            end
        else
            p1=randn/sqrt(r+hr);
            %p1=randn/sqrt(r);
            p2=randn/sqrt(r);
            if(p2>=p1+1)
                i=i-1;
                hr=0;
            end
        end
        evals=evals+r*2;
        hr=hr+r;
    end
    E=[E evals];
end
end