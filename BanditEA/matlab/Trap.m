function res=Trap(C,X)
f=C;
for x=X
    f=f*(1-x);
end
res=f+sum(x);
end