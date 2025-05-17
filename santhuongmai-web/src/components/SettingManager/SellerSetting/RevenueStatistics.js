import React, { useState, useEffect } from 'react';
import { Container, Form, Row, Col, Alert, Button } from 'react-bootstrap';
import { Bar, Line, Pie } from 'react-chartjs-2';
import { Chart as ChartJS, CategoryScale, LinearScale, BarElement, LineElement, PointElement, ArcElement, Title, Tooltip, Legend } from 'chart.js';
import { toast } from 'react-toastify';
import Apis, { authApis, endpoints } from '../../../configs/Apis';
import MySpinner from '../../layouts/MySpinner';
import { useSelector } from 'react-redux';

ChartJS.register(CategoryScale, LinearScale, BarElement, LineElement, PointElement, ArcElement, Title, Tooltip, Legend);

const RevenueStatistics = () => {
    const [period, setPeriod] = useState('month');
    const [year, setYear] = useState('2025');
    const [chartType, setChartType] = useState('bar'); // Thêm trạng thái cho loại biểu đồ
    const [data, setData] = useState([]);
    const [labels, setLabels] = useState([]);
    const [loading, setLoading] = useState(false);
    const user = useSelector(state => state.authentication);
    const [seller, setSeller] = useState({});

    const fetchRevenueStatistics = async () => {
        setLoading(true);
        try {
            const response = await authApis().get(endpoints.revenueStatistics(period, year, '', seller?.shopId));
            console.log("thong ke");
            console.log(response.data);
            setData(response.data.data);
            setLabels(response.data.labels);
        } catch (err) {
            toast.error('Lỗi khi tải dữ liệu thống kê: ' + (err.response?.data?.error || err.message));
        } finally {
            setLoading(false);
        }
    };

    const loadSeller = async () => {
        try {
            const res = await Apis.get(endpoints['seller-with-id'](user.id));
            setSeller(res.data);
        } catch (error) {
            toast.error(error);
        }
    };

    useEffect(() => {
        loadSeller();
    }, []);

    useEffect(() => {
        if (seller?.shopId != null) {
            fetchRevenueStatistics();
        }
    }, [period, year, seller]);

    const chartData = {
        labels: labels,
        datasets: [
            {
                label: 'Doanh thu (VND)',
                data: data,
                backgroundColor: chartType === 'pie' 
                    ? ['rgba(75, 192, 192, 0.6)', 'rgba(255, 99, 132, 0.6)', 'rgba(54, 162, 235, 0.6)', 'rgba(255, 206, 86, 0.6)'] 
                    : 'rgba(75, 192, 192, 0.6)',
                borderColor: chartType === 'pie' 
                    ? ['rgba(75, 192, 192, 1)', 'rgba(255, 99, 132, 1)', 'rgba(54, 162, 235, 1)', 'rgba(255, 206, 86, 1)'] 
                    : 'rgba(75, 192, 192, 1)',
                borderWidth: 1,
                fill: chartType === 'line', // Fill cho biểu đồ Line
            },
        ],
    };

    const options = {
        responsive: true,
        plugins: {
            legend: {
                position: 'top',
            },
            title: {
                display: true,
                text: `Thống kê doanh thu theo ${period === 'month' ? 'tháng' : period === 'quarter' ? 'quý' : 'danh mục'}`,
            },
        },
        scales: chartType === 'pie' ? {} : {
            y: {
                beginAtZero: true,
                title: {
                    display: true,
                    text: 'Doanh thu (VND)',
                },
            },
            x: {
                title: {
                    display: true,
                    text: period === 'year' ? 'Danh mục' : period === 'quarter' ? 'Quý' : 'Tháng',
                },
            },
        },
    };

    const renderChart = () => {
        switch (chartType) {
            case 'bar':
                return <Bar data={chartData} options={options} />;
            case 'line':
                return <Line data={chartData} options={options} />;
            case 'pie':
                return <Pie data={chartData} options={options} />;
            default:
                return <Bar data={chartData} options={options} />;
        }
    };

    return (
        <Container className="py-4">
            <h4>Thống kê Doanh thu</h4>
            {loading ? (
                <MySpinner />
            ) : (
                <>
                    <Row className="mb-4">
                        <Col md={3}>
                            <Form.Group controlId="periodSelect">
                                <Form.Label>Khoảng thời gian</Form.Label>
                                <Form.Select value={period} onChange={(e) => setPeriod(e.target.value)}>
                                    <option value="month">Theo tháng</option>
                                    <option value="quarter">Theo quý</option>
                                    <option value="year">Theo danh mục</option>
                                </Form.Select>
                            </Form.Group>
                        </Col>
                        <Col md={3}>
                            <Form.Group controlId="yearSelect">
                                <Form.Label>Năm</Form.Label>
                                <Form.Select value={year} onChange={(e) => setYear(e.target.value)}>
                                    <option value="2025">2025</option>
                                    <option value="2024">2024</option>
                                    <option value="2023">2023</option>
                                </Form.Select>
                            </Form.Group>
                        </Col>
                        <Col md={3}>
                            <Form.Group controlId="chartTypeSelect">
                                <Form.Label>Loại biểu đồ</Form.Label>
                                <Form.Select value={chartType} onChange={(e) => setChartType(e.target.value)}>
                                    <option value="bar">Biểu đồ cột</option>
                                    <option value="line">Biểu đồ đường</option>
                                    <option value="pie">Biểu đồ tròn</option>
                                </Form.Select>
                            </Form.Group>
                        </Col>
                        <Col md={3} className="d-flex align-items-end">
                            <Button variant="success" onClick={fetchRevenueStatistics} disabled={loading}>
                                Tải lại dữ liệu
                            </Button>
                        </Col>
                    </Row>
                    {data.length === 0 ? (
                        <Alert variant="info">Không có dữ liệu thống kê.</Alert>
                    ) : (
                        <div style={{ maxWidth: '800px', margin: '0 auto' }}>
                            {renderChart()}
                        </div>
                    )}
                </>
            )}
        </Container>
    );
};

export default RevenueStatistics;