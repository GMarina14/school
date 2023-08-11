create TABLE cars(
    brand TEXT,
    model TEXT PRIMARY KEY,
    price NUMERIC(15,2)
);
create TABLE humans(
    vehicle TEXT REFERENCES cars(model),
    name TEXT NOT NULL PRIMARY KEY,
    age INTEGER ,
    driverLicense BOOLEAN
);
select humans.name, humans.age, cars.brand, cars.price from humans INNER JOIN cars ON humans.vehicle = cars.model;


select student.name, student.age, faculty.name from student INNER JOIN faculty ON student.faculty_id = faculty.id;
select student.name from student inner join avatar a on student.id = a.student_id;